package pl.lawit.domain;

import io.vavr.collection.List;
import lombok.Builder;
import lombok.NonNull;

import java.util.function.Function;

@Builder(toBuilder = true)
public record PageResultFireBase<T>(
        @NonNull
        List<T> content,

        @NonNull
        String nextPageToken
) {
    public <R> PageResultFireBase<R> map(Function<? super T, ? extends R> mapper) {
        return new PageResultFireBase<>(
                content.map(mapper),
                nextPageToken
        );
    }
}