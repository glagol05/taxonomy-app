package models;

public record CreatureImage(
    int id,
    int creatureId,
    String path,
    String caption,
    boolean isStandard,
    String source,
    String attribution,
    Integer sortOrder
) {}