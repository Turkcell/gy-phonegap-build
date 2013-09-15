/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ttech.cordovabuild.web;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ttech.cordovabuild.domain.application.Application;
import com.ttech.cordovabuild.domain.application.*;
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
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@Produces({MediaType.APPLICATION_JSON})
public class ApplicationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationResource.class);
    @Autowired
    ApplicationService service;
    @Autowired
    BuiltQueuePublisher queuePublisher;

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
    @Path("/{id}/download/{type}")
    @Produces({BuiltType.Constants.ANDROID_MIME_TYPE, BuiltType.Constants.IOS_MIME_TYPE})
    public Response getBuiltAsset(@PathParam("id") Long id, @PathParam("type") BuiltType type) {
        ApplicationBuilt latestBuilt = service.getLatestBuilt(id);
        for (BuiltTarget builtTarget : latestBuilt.getBuiltTargets()) {
            if (builtTarget.getType().equals(type))
                if (builtTarget.getStatus().equals(BuiltTarget.Status.SUCCESS)) {
                    Response.accepted().
                            type(type.getMimeType()).
                            header("content-disposition",
                                    MessageFormat.format("attachment; filename = {0}.{1}",
                                            latestBuilt.getBuiltConfig().getApplicationName(), type.getPlatformSuffix())).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
        }
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
}
