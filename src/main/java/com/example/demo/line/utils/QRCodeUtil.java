package com.example.demo.line.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
@Slf4j
public class QRCodeUtil {

	public InputStreamSource getQrCodeStream(String qrCodeContent) {
		QRCodeWriter barcodeWriter = new QRCodeWriter();

		BitMatrix bitMatrix = null;
		try {
			bitMatrix = barcodeWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, 200, 200);
		} catch (WriterException e) {
			log.error("create qrCode error");
		}
		BufferedImage qrCode = MatrixToImageWriter.toBufferedImage(bitMatrix);

		return () -> {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(qrCode, "png", os);
			return new ByteArrayInputStream(os.toByteArray());
		};
	}
}
