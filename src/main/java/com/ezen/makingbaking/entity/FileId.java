package com.ezen.makingbaking.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class FileId implements Serializable {
	private int fileNo;
	private int fileReferNo;
	private String fileType;
}
