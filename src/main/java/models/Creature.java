package models;

public record Creature (
    int id,
    String domain,
    String kingdom,
    String phylum,
    String className,
    String order,
    String family,
    String genus,
    String species
) {}
