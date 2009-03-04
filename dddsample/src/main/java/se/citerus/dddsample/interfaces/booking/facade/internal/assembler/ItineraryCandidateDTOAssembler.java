package se.citerus.dddsample.interfaces.booking.facade.internal.assembler;

import se.citerus.dddsample.domain.model.cargo.Itinerary;
import se.citerus.dddsample.domain.model.cargo.Leg;
import se.citerus.dddsample.domain.model.carrier.Voyage;
import se.citerus.dddsample.domain.model.carrier.VoyageNumber;
import se.citerus.dddsample.domain.model.carrier.VoyageRepository;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.model.location.LocationRepository;
import se.citerus.dddsample.domain.model.location.UnLocode;
import se.citerus.dddsample.interfaces.booking.facade.dto.LegDTO;
import se.citerus.dddsample.interfaces.booking.facade.dto.RouteCandidateDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Assembler class for the ItineraryCandidateDTO.
 */
public class ItineraryCandidateDTOAssembler {
  
  public RouteCandidateDTO toDTO(final Itinerary itinerary) {
    final List<LegDTO> legDTOs = new ArrayList<LegDTO>(itinerary.legs().size());
    for (Leg leg : itinerary.legs()) {
      legDTOs.add(toLegDTO(leg));
    }
    return new RouteCandidateDTO(legDTOs);
  }

  protected LegDTO toLegDTO(final Leg leg) {
    final VoyageNumber voyageNumber = leg.voyage().voyageNumber();
    final UnLocode from = leg.loadLocation().unLocode();
    final UnLocode to = leg.unloadLocation().unLocode();
    return new LegDTO(voyageNumber.idString(), from.idString(), to.idString(), leg.loadTime(), leg.unloadTime());
  }

  public Itinerary fromDTO(RouteCandidateDTO routeCandidateDTO, VoyageRepository voyageRepository, LocationRepository locationRepository) {
    final List<Leg> legs = new ArrayList<Leg>(routeCandidateDTO.getLegs().size());
    for (LegDTO legDTO : routeCandidateDTO.getLegs()) {
      final VoyageNumber voyageNumber = new VoyageNumber(legDTO.getVoyageNumber());
      final Voyage voyage = voyageRepository.find(voyageNumber);
      final Location from = locationRepository.find(new UnLocode(legDTO.getFrom()));
      final Location to = locationRepository.find(new UnLocode(legDTO.getTo()));
      legs.add(new Leg(voyage, from, to, new Date(), new Date()));  // TODO better dates
    }
    return new Itinerary(legs);
  }
}