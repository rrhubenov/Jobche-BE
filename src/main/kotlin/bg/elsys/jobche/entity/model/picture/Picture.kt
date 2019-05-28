package bg.elsys.jobche.entity.model.picture

import bg.elsys.jobche.entity.BaseEntity
import java.util.*

abstract class Picture(val pictureId: String = UUID.randomUUID().toString()): BaseEntity()
