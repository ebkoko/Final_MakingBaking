package com.ezen.makingbaking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadyResponseDTO {
	private String tid;
	private String next_redirect_pc_url;
	private String partner_order_id;
	public long OrderNo;
	public long ReserNo;
}
