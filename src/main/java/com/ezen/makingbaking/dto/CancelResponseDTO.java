package com.ezen.makingbaking.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelResponseDTO {
	private String tid;
	private String next_redirect_pc_url;
	private String partner_order_id;
	public long OrderNo;
	public long ReserNo;
}
