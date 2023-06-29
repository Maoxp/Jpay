package com.github.maoxp.wx.model.v3.basepay.pay;

import com.github.maoxp.wx.model.v3.BaseModel;
import com.github.maoxp.wx.model.v3.basepay.pay.internal.Amount;
import com.github.maoxp.wx.model.v3.basepay.pay.internal.Detail;
import com.github.maoxp.wx.model.v3.basepay.pay.internal.SceneInfo;
import com.github.maoxp.wx.model.v3.basepay.pay.internal.SettleInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AppUnifiedOrderModel
 * <p>APP支付-下单 Model</p>
 *
 * @author mxp
 * @date 2023/6/15年06月15日 16:51
 * @since JDK 1.8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppUnifiedOrder extends BaseModel {
    /**
     * 应用ID
     *
     * <p>必填：是</p>
     * <p>POST：body</p>
     * <p>由微信生成的应用ID，全局唯一。请求基础下单接口时请注意APPID的应用属性，直连模式下该id应为APP应用的id。</p>
     */
    private String appid;

    /**
     * 直连商户号
     *
     * <p>必填：是</p>
     * <p>POST：body</p>
     * <p>直连商户的商户号，由微信支付生成并下发。</p>
     */
    private String mchid;

    /**
     * 商品描述
     * <p>必填：是</p>
     * <p>POST：body</p>
     */
    private String description;
    /**
     * 商户订单号
     * <p>必填：是</p>
     * <p>POST：body</p>
     */
    private String out_trade_no;

    /**
     * 交易结束时间
     * <p>必填：否</p>
     * <p>POST：body</p>
     * <p>
     *     订单失效时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
     * </p>
     * <p>示例值：2018-06-08T10:34:56+08:00</p>
     */
    private String time_expire;
    /**
     * 附加数据
     * <p>必填：否</p>
     * <p>POST：body</p>
     * <p>附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用，实际情况下只有支付完成状态才会返回该字段。</p>
     */
    private String attach;

    /**
     * 通知地址,要求必须为https地址。请确保回调URL是外部可正常访问的，且不能携带后缀参数
     * 支付结果通知是以POST 方法访问商户设置的通知url，通知的数据以JSON 格式通过请求主体（BODY）传输。
     * 通知的数据包括了加密的支付结果详情。
     *
     * <p>必填：是</p>
     * <p>POST：body</p>
     * <p>
     * <em>（注：由于涉及到回调加密和解密，商户必须先设置好apiv3秘钥后才能解密回调通知，apiv3秘钥设置文档指引
     * <a href="https://kf.qq.com/faq/180830E36vyQ180830AZFZvu.html" >详见APIv3秘钥设置指引</a>）
     * </p>
     */
    private String notify_url;

    /**
     * 订单优惠标记
     * <p>必填：否</p>
     * <p>POST：body</p>
     */
    private String goods_tag;
    /**
     * 电子发票入口开放标识
     * <p>必填：否</p>
     * <p>POST：body</p>
     * <p>传入true时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效。</p>
     */
    private boolean support_fapiao;

    /**
     * 订单金额
     * <p>必填：是</p>
     * <p>POST：body</p>
     */
    private Amount amount;

    /**
     * 优惠功能
     * <p>必填：否</p>
     * <p>POST：body</p>
     */
    private Detail detail;
    /**
     * 场景信息
     * <p>必填：否</p>
     * <p>POST：body</p>
     */
    private SceneInfo scene_info;
    /**
     * 结算信息
     * <p>必填：否</p>
     * <p>POST：body</p>
     */
    private SettleInfo settle_info;


}
