package pl.lawit.scheduler;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import org.quartz.Job;

import java.util.Date;

@Builder(toBuilder = true)
public record ScheduleProperties(

	@NonNull
	String key,

	@NonNull
	Class<? extends Job> jobClass,

	@NonNull
	Date scheduleTime,

	@NonNull
	ScheduleGroup scheduleGroup,

	@NonNull
	Option<String> description

) {
}
