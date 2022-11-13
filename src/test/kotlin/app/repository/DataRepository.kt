package app.repository

import app.model.Entity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DataRepository : JpaRepository<Entity, UUID>
