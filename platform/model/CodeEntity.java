package platform.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CodeEntity {
    @Id
    @JsonIgnore
    private UUID uuid = UUID.randomUUID();
    @Column(length = 1000)
    private String code;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date = LocalDateTime.now();
    @JsonIgnore
    private boolean timeRestricted;
    @JsonIgnore
    private boolean viewsRestricted;
    private long time;
    private int views;
    @JsonIgnore
    private LocalDateTime endDateTimeSecretCode;
}
