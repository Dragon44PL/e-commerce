package users.vo;

import domain.vo.EventEntitySnapshot;
import users.events.UserEvent;
import java.util.List;
import java.util.UUID;

public record UserSnapshot(UUID id, AccountId account, UserInfo userInfo, List<UserEvent> events) implements EventEntitySnapshot<UserEvent> { }
