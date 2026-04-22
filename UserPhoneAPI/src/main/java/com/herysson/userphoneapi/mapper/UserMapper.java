package com.herysson.userphoneapi.mapper;

import com.herysson.userphoneapi.dto.CreateUserRequestDTO;
import com.herysson.userphoneapi.dto.PhoneResponseDTO;
import com.herysson.userphoneapi.dto.UserResponseDTO;
import com.herysson.userphoneapi.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercício 3 – Classe de mapeamento para User
 *
 * Aqui reaproveito o PhoneMapper para converter a lista de Phone
 * junto com o User, em vez de repetir a lógica de conversão.
 * Isso mantém o código mais limpo e fácil de manter.
 */
public class UserMapper {

    private UserMapper() {}

    /**
     * Converte a entidade User (com sua lista de telefones) para UserResponseDTO.
     *
     * O stream + map chama o PhoneMapper para cada telefone da lista,
     * gerando uma lista de PhoneResponseDTO pronta para a resposta.
     *
     * Direção: Entidade → DTO (saída)
     */
    public static UserResponseDTO toDTO(User user) {
        List<PhoneResponseDTO> phoneDTOs = user.getPhones() != null
                ? user.getPhones().stream()
                        .map(PhoneMapper::toDTO)
                        .toList()
                : new ArrayList<>();

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                phoneDTOs
        );
    }

    /**
     * Converte CreateUserRequestDTO em entidade User.
     *
     * Os telefones NÃO são convertidos aqui porque precisam receber
     * a referência do usuário (phone.setUser(user)) — e nesse ponto
     * o user ainda não foi salvo/gerou id. Essa lógica fica no service.
     *
     * Direção: DTO (entrada) → Entidade
     */
    public static User toEntity(CreateUserRequestDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        // phones são tratados no UserService para manter o vínculo correto
        return user;
    }
}
