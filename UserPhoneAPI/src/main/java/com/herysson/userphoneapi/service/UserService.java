package com.herysson.userphoneapi.service;

import com.herysson.userphoneapi.dto.CreateUserRequestDTO;
import com.herysson.userphoneapi.dto.UpdatePhoneRequestDTO;
import com.herysson.userphoneapi.dto.UserResponseDTO;
import com.herysson.userphoneapi.mapper.PhoneMapper;
import com.herysson.userphoneapi.mapper.UserMapper;
import com.herysson.userphoneapi.model.Phone;
import com.herysson.userphoneapi.model.User;
import com.herysson.userphoneapi.repository.PhoneRepository;
import com.herysson.userphoneapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Exercício 5 – Service de usuário adaptado para trabalhar com DTOs
 *
 * Tirei toda a lógica de negócio do controller e trouxe para cá.
 * O controller agora só cuida de HTTP (receber requisição, devolver resposta),
 * enquanto o service cuida de como os dados são processados.
 *
 * Também é aqui que resolvo o relacionamento entre User e Phone:
 * após converter o DTO para entidade, associo cada phone ao user
 * antes de salvar — o JPA precisa desse vínculo para gerar a FK corretamente.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    public UserService(UserRepository userRepository, PhoneRepository phoneRepository) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
    }

    /**
     * Retorna todos os usuários já convertidos para DTO.
     * O controller não precisa saber nada sobre a entidade User.
     */
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    /**
     * Busca um usuário por id e retorna como DTO.
     * Optional é mantido para o controller decidir o status HTTP.
     */
    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id).map(UserMapper::toDTO);
    }

    /**
     * Exercício 5 – Cadastro de usuário com múltiplos telefones
     *
     * O fluxo é:
     * 1. Converter o DTO para entidade User (sem phones ainda)
     * 2. Inicializar a lista de phones no user
     * 3. Para cada CreatePhoneRequestDTO, converter para Phone e definir o user
     * 4. Salvar o user (o cascade ALL já salva os phones junto)
     * 5. Retornar o UserResponseDTO com os dados persistidos
     */
    public UserResponseDTO createUser(CreateUserRequestDTO dto) {
        User user = UserMapper.toEntity(dto);
        user.setPhones(new ArrayList<>());

        if (dto.phones() != null && !dto.phones().isEmpty()) {
            List<Phone> phones = dto.phones().stream()
                    .map(phoneDTO -> {
                        Phone phone = PhoneMapper.toEntity(phoneDTO);
                        // vincular o telefone ao usuário antes de salvar
                        phone.setUser(user);
                        return phone;
                    })
                    .toList();

            user.getPhones().addAll(phones);
        }

        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

    /**
     * Remove um usuário pelo id.
     * Retorna true se encontrou e deletou, false se não existia.
     */
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Exercício 7 – Atualização de telefone usando UpdatePhoneRequestDTO
     *
     * Busca o phone existente pelo id, aplica os campos do DTO
     * via PhoneMapper.updateEntityFromDTO, e salva.
     * Não cria um objeto novo — atualiza o que já está no banco.
     */
    public Optional<UserResponseDTO> updatePhone(Long userId, Long phoneId, UpdatePhoneRequestDTO dto) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        Optional<Phone> phoneOpt = phoneRepository.findById(phoneId);

        if (phoneOpt.isEmpty()) {
            return Optional.empty();
        }

        Phone phone = phoneOpt.get();

        // verifica se o telefone realmente pertence a esse usuário
        if (!phone.getUser().getId().equals(userId)) {
            return Optional.empty();
        }

        PhoneMapper.updateEntityFromDTO(dto, phone);
        phoneRepository.save(phone);

        // recarrega o usuário para refletir as mudanças na resposta
        User updatedUser = userRepository.findById(userId).get();
        return Optional.of(UserMapper.toDTO(updatedUser));
    }
}
