package it.androidclient.UserCtx

import java.time.LocalDateTime

object HumanRelationshipModel {
    data class UserRelations(var relationships: List<Pair<HumanRelationKind, String>>)

    enum class HumanRelationKind {
        None,
        Father,
        Mother,
        Grandfather,
        Grandmother,
        Son,
        Daughter,
        Grandson,
        Granddaughter,
        Uncle,
        Aunt,
        Cousin
    }
}