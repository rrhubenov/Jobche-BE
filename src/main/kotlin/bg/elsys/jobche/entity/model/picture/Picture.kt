package bg.elsys.jobche.entity.model.picture

import bg.elsys.jobche.entity.BaseEntity
import org.jetbrains.annotations.NotNull
import java.util.*
import javax.persistence.Entity
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class Picture(@NotNull
                       val pictureId: String = UUID.randomUUID().toString()): BaseEntity()
