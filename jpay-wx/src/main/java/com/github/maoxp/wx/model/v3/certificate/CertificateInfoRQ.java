package com.github.maoxp.wx.model.v3.certificate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 * <p>证书响应参数 Model</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class CertificateInfoRQ implements Serializable {

	private static final long serialVersionUID = -5838236796703654715L;

	private String serial_no;
	private String effective_time;
	private String expire_time;
	private EncryptCertificateRQ encrypt_certificate;
}
