package com.github.maoxp.pay.client.page;

import lombok.Data;

import java.io.Serializable;

/**
 * PageRequest
 *
 * @author mxp
 * @since JDK 1.8
 */
@Data
public class PageRequest implements Serializable {
    private static final long serialVersionUID = 8721466590911281916L;

    private Integer currentPage = 1;
    private Integer pageSize = 10;
}
