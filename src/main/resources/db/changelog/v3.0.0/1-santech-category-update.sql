UPDATE sanparser.attribute_cache
SET json_value = '{"value":"' || REPLACE(REPLACE(REPLACE(json_value, '\', '\\'), '"', '\"'), CHR(0), '') || '"}'
WHERE attribute_key = 'category';