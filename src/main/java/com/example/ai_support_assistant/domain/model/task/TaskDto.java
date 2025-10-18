package com.example.ai_support_assistant.domain.model.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
public class TaskDto {

	@JsonProperty("TaskID")
	private String id;

	@JsonProperty("TaskInfo")
	private TaskInfo taskInfo;

}

