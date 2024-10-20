package ubb.scs.map.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class Friendship extends Entity<Tuple<Long,Long>> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private LocalDateTime date;
    private final Long idUser1;
    private final Long idUser2;

    public Friendship(Long idUser1, Long idUser2) {
        this.idUser1 = min(idUser1, idUser2);
        this.idUser2 = max(idUser2, idUser1);
    }

    public Long getIdUser1() {
        return idUser1;
    }
    public Long getIdUser2() {
        return idUser2;
    }

    public String toString() {
        return "Friendship between users " + idUser1 + " and " + idUser2 + " established at: " + date.format(formatter);
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * @param date - the date when the friendship was created
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
