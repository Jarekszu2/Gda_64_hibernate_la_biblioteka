package biblioteka.packModel;


import biblioteka.packUtil.IBaseEntity;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book implements IBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private int yearWritten;
    @Formula(value = "(year(now()) - yearWritten)")
//    @Formula(value = "(year(now()) - year(yearWritten))") gdy data jako LocalData
    private Integer howOld;
    @Column(nullable = false)
    private int numberOfPages;
    @Column(nullable = false)
    private int numberOfAvailableCopies;
    @Formula(value = "(select count(*) from BookLent bl where bl.book_id - id and bl.dateReturned is null)")
    private int numberOfBorrowedCopies;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "books")
    private Set<Author> authors;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private Set<BookLent> currentLents;
}
