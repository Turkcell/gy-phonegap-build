/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ttech.cordovabuild.web;

import com.google.common.io.ByteStreams;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ttech.cordovabuild.domain.application.Application;
import com.ttech.cordovabuild.domain.application.*;
import com.ttech.cordovabuild.domain.asset.AssetRef;
import com.ttech.cordovabuild.domain.asset.AssetService;
import com.ttech.cordovabuild.domain.asset.InputStreamHandler;
import com.ttech.cordovabuild.infrastructure.queue.BuiltQueuePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.List;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@Produces({MediaType.APPLICATION_JSON})
public class ApplicationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationResource.class);
    @Autowired
    ApplicationService service;
    @Autowired
    BuiltQueuePublisher queuePublisher;
    @Autowired
    AssetService assetService;

    @GET
    public List<ApplicationBuilt> getApplications() {
        return service.getApplications(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GET
    @Path("/{id}")
    public ApplicationBuilt getApplication(@PathParam("id") Long id) {
        LOGGER.info("get application with {} and application service {}", id, service);
        return service.getLatestBuilt(id);
    }

    @PUT
    @Path("/{id}")
    public Application updateApplication(@PathParam("id") Long id) {
        return service.updateApplicationCode(id);
    }

    @POST
    @Path("/{id}/build")
    public ApplicationBuilt rebuildApplication(@PathParam("id") Long id) {
        ApplicationBuilt applicationBuilt = service.prepareApplicationBuilt(id);
        queuePublisher.publishBuilt(applicationBuilt);
        return applicationBuilt;
    }

    @GET
    @Path("/{id}/qrimage")
    @Produces("image/jpeg")
    public StreamingImageOutput getQrImage(@PathParam("id") Long id, @QueryParam("width") @DefaultValue("150") int width, @QueryParam("height") @DefaultValue("150") int height, @Context UriInfo uriInfo) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String url = uriInfo.getRequestUriBuilder().path("..").path("install").queryParam("qrKey", service.findApplication(id).getQrKey()).build().normalize().toString();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            return new StreamingImageOutput(image);
        } catch (WriterException e) {
            throw new QrCodeException(e);
        }
    }

    @GET
    @Path("/{id}/icon")
    @Produces("image/png")
    public StreamingImageOutput getIcon(@PathParam("id") Long id){
        return null;
    }

    @GET
    @Path("/{id}/download/{type}")
    @Produces({BuiltType.Constants.ANDROID_MIME_TYPE, BuiltType.Constants.IOS_MIME_TYPE})
    public Response getBuiltAsset(@PathParam("id") Long id, @PathParam("type") BuiltType type, @Context HttpServletResponse httpServletResponse) throws IOException {
        LOGGER.info("application asset download for id:{} and type:{} requested", id, type);
        ApplicationBuilt latestBuilt = service.getLatestBuilt(id);
        LOGGER.info("application built with id:{} found", latestBuilt.getId());
        for (BuiltTarget builtTarget : latestBuilt.getBuiltTargets()) {
            if (builtTarget.getType().equals(type)) {
                LOGGER.info("built target for {} found with status {}", builtTarget.getType(), builtTarget.getStatus());
                if (builtTarget.getStatus().equals(BuiltTarget.Status.SUCCESS)) {
                    httpServletResponse.setHeader("Content-Type", type.getMimeType());
                    httpServletResponse.setHeader("content-disposition", MessageFormat.format("attachment; filename = {0}.{1}",
                            latestBuilt.getBuiltConfig().getApplicationName(), type.getPlatformSuffix()));
                    write(httpServletResponse.getOutputStream(), builtTarget.getAssetRef());
                    return Response.ok().build();
                } else {
                    httpServletResponse.setStatus(Response.Status.NOT_FOUND.getStatusCode());
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }
        }
        LOGGER.info("no build target found for {}", type);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public ApplicationBuilt createApplication(@QueryParam("sourceUri") String sourceUri) {
        Application application = service.createApplication(SecurityContextHolder.getContext().getAuthentication().getName(), sourceUri);
        ApplicationBuilt applicationBuilt = service.prepareApplicationBuilt(application);
        queuePublisher.publishBuilt(applicationBuilt);
        return applicationBuilt;
    }

    class StreamingImageOutput implements StreamingOutput {
        private BufferedImage image;

        public StreamingImageOutput(BufferedImage image) {
            this.image = image;
        }

        public void write(OutputStream output) throws IOException, WebApplicationException {
            JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
            jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(1f);
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            MemoryCacheImageOutputStream out = new MemoryCacheImageOutputStream(output);
            writer.setOutput(out);
            writer.write(null, new IIOImage(image, null, null), jpegParams);
            writer.dispose();
            out.close();
        }
    }

    private void write(final OutputStream output, AssetRef assetRef) throws IOException, WebApplicationException {
        assetService.handleInputStream(assetRef, new InputStreamHandler() {
            @Override
            public void handleInputStream(InputStream inputStream) throws IOException {
                ByteStreams.copy(inputStream, output);
            }
        });
    }

}
