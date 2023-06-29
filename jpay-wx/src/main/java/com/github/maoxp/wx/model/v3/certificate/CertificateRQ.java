package com.github.maoxp.wx.model.v3.certificate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>证书响应参数 Model</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class CertificateRQ implements Serializable {
    private static final long serialVersionUID = 4034303177841190752L;

    private List<CertificateInfoRQ> data;
}
