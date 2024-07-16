package mobex;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "currency")
@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long currency_id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String symbol;


}




