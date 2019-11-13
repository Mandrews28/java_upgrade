package got;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface MemberDAO {
    Member findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAllByHouse(House house);
    Stream<Member> findAllByHouseName(String houseName);
    Collection<Member> getAll();
}
