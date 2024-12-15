package me.supcheg.sanparser.data;

import me.supcheg.sanparser.id.SantechIdentifier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface IdentifiedUriMapper {

    IdentifiedUri santechIdentifierToIdentifiedUrl(SantechIdentifier identifier);

    SantechIdentifier identifiedUrlToSantechIdentifier(IdentifiedUri identifiedUri);
}
