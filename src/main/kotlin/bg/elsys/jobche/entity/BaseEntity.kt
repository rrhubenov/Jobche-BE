package bg.elsys.jobche.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(value = ["createdAt", "updatedAt"], allowGetters = true)
abstract class BaseEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false, updatable = false)
        val id: Long = 0,

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "created_at", nullable = false, updatable = false)
        @CreatedDate
        val createdAt: Date = Date(),

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "updated_at", nullable = false)
        @LastModifiedDate
        val updatedAt: Date = Date()) : Serializable