create table sanparser.attribute_cache
(
    uri         varchar primary key,
    attribute_key varchar not null,
    json_value     varchar
)