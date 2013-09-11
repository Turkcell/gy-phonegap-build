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
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.ttech.cordovabuild.domain.application.Application;
import com.ttech.cordovabuild.domain.application.ApplicationBuilt;
import com.ttech.cordovabuild.domain.application.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@Produces({MediaType.APPLICATION_JSON})
public class ApplicationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationResource.class);
    @Autowired
    ApplicationService service;

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
        //TODO publish application built
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

    @POST
    public ApplicationBuilt createApplication(@QueryParam("sourceUri") String sourceUri) {
        Application application = service.createApplication(SecurityContextHolder.getContext().getAuthentication().getName(), sourceUri);
        //TODO publish application built
        return service.prepareApplicationBuilt(application);
    }

    class StreamingImageOutput implements StreamingOutput {
        private BufferedImage image;

        public StreamingImageOutput(BufferedImage image) {
            this.image = image;
        }

        public void write(OutputStream output) throws IOException, WebApplicationException {
            JPEGImageEncoder jencoder = JPEGCodec.createJPEGEncoder(output);
            JPEGEncodeParam enParam = jencoder.getDefaultJPEGEncodeParam(image);
            enParam.setQuality(1.0F, true);
            jencoder.setJPEGEncodeParam(enParam);
            jencoder.encode(image);
            output.close();
        }
    }
}
