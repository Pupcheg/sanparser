create table sanparser.attribute_cache
(
    id            uuid primary key,
    uri           varchar not null,
    attribute_key varchar not null,
    json_value    varchar
)