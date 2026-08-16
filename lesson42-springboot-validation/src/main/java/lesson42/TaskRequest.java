package lesson42;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// DTO = data sent by the client (not the DB entity itself)
public class TaskRequest {

    @NotBlank(message = "title is required")
    @Size(min = 3, max = 100, message = "title must be 3-100 characters")
    private String title;

    // Required only when validating with OnUpdate group (PUT)
    @NotNull(groups = OnUpdate.class, message = "done is required")
    private Boolean done;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
