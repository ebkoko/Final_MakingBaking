package com.ezen.makingbaking.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelResponseDTO {
	private String aid;
	private String tid;
	private String cid;
	private String status;
	private String next_redirect_pc_url;
	private String partner_order_id;
	private String partner_user_id;
	private String payment_method_type;
	private AmountDTO amount;
	private String item_name;
	private String item_code;
	private int quantity;
	private String created_at;
	private String approved_at;
	private String canceled_at;
	private String payload;
}
