package fr.keyser.wonderfull.web;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import fr.keyser.wonderfull.world.PlayerGameDescription;
import fr.keyser.wonderfull.world.action.ActionAffectToProduction;
import fr.keyser.wonderfull.world.action.ActionDraft;
import fr.keyser.wonderfull.world.action.ActionMoveDraftedToProduction;
import fr.keyser.wonderfull.world.action.ActionRecycleDrafted;
import fr.keyser.wonderfull.world.action.ActionRecycleDraftedToProduction;
import fr.keyser.wonderfull.world.action.ActionRecycleProduction;
import fr.keyser.wonderfull.world.action.ActionSupremacy;
import fr.keyser.wonderfull.world.dto.GameDTO;
import fr.keyser.wonderfull.world.game.InGameService;

@Controller
public class GamingController {

	private final InGameService service;

	public GamingController(InGameService service) {
		this.service = service;
	}

	@SubscribeMapping("/my-games")
	public List<PlayerGameDescription> gamesFor(Principal principal) {
		return service.gamesFor(principal.getName());
	}

	@SubscribeMapping("/game/{externalId}")
	public GameDTO connect(@DestinationVariable String externalId) {
		return service.refresh(externalId);
	}

	@MessageMapping("/game/{externalId}/pass")
	public void pass(@DestinationVariable String externalId) {
		service.pass(externalId);
	}

	@MessageMapping("/game/{externalId}/draft")
	public void draft(@DestinationVariable String externalId, @Payload ActionDraft action) {
		service.play(externalId, action);
	}

	@MessageMapping("/game/{externalId}/moveToProduction")
	public void moveToProduction(@DestinationVariable String externalId,
			@Payload ActionMoveDraftedToProduction action) {
		service.play(externalId, action);
	}

	@MessageMapping("/game/{externalId}/recycleDraft")
	public void recycleDraft(@DestinationVariable String externalId, @Payload ActionRecycleDrafted action) {
		service.play(externalId, action);
	}

	@MessageMapping("/game/{externalId}/recycleDraftedToProduction")
	public void recycleDraftedToProduction(@DestinationVariable String externalId,
			@Payload ActionRecycleDraftedToProduction action) {
		service.play(externalId, action);
	}

	@MessageMapping("/game/{externalId}/supremacy")
	public void supremacy(@DestinationVariable String externalId, @Payload ActionSupremacy action) {
		service.play(externalId, action);
	}

	@MessageMapping("/game/{externalId}/affectation")
	public void affectation(@DestinationVariable String externalId, @Payload ActionAffectToProduction action) {
		service.play(externalId, action);
	}

	@MessageMapping("/game/{externalId}/recycleToProduction")
	public void recycleToProduction(@DestinationVariable String externalId,
			@Payload ActionRecycleDraftedToProduction action) {
		service.play(externalId, action);
	}

	@MessageMapping("/game/{externalId}/recycleProduction")
	public void recycleProduction(@DestinationVariable String externalId, @Payload ActionRecycleProduction action) {
		service.play(externalId, action);
	}

}
