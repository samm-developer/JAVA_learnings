package lesson53;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private boolean done;

    /**
     * Many tasks → one user (owner).
     * DB column: owner_id (foreign key to users.id)
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    @JsonIgnore // don't dump whole user (password!) into JSON
    private AppUser owner;

    public Task() {
    }

    public Task(String title, boolean done, AppUser owner) {
        this.title = title;
        this.done = done;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }

    /** Shown in JSON as "ownerUsername" */
    @JsonProperty("ownerUsername")
    public String getOwnerUsername() {
        return owner != null ? owner.getUsername() : null;
    }
}
