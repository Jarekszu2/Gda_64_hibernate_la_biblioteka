package biblioteka.packModel;

import biblioteka.packUtil.IBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author implements IBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // jak zmienimy AUTO na IDENTITY to będzie numerować od jedynki
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    // Z powodu relacji ManyToMany mamy tabelę pośredniczącą o nazwie Author_Book.
    // zapytanie musi dotyczyć tabeli pośredniczącej i zliczyć wystąpienia autora w tej tabeli
    @Formula("(select count(*) from Author_Book ab where ab.authors_id = id)")
    private Long numberOfBooks;

    // możemy z tej strony dodawać (książki do autorów) żeby tworzyć relacje
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Book> books = new HashSet<>();

}
