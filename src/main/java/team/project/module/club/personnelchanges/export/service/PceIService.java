package team.project.module.club.personnelchanges.export.service;

public interface PceIService {

    boolean isClubManager(String userId, Long clubId);

    boolean isClubMember(String userId, Long clubId);
}
