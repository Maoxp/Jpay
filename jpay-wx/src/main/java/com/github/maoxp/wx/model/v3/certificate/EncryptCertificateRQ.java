package com.github.maoxp.wx.model.v3.certificate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Javen
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class EncryptCertificateRQ implements Serializable {

    private static final long serialVersionUID = -5718842307268149730L;

    private String algorithm;
    private String nonce;
    private String associated_data;
    private String ciphertext;
}
