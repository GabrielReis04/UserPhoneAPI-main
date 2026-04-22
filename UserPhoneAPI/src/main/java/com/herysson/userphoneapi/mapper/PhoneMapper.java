package com.herysson.userphoneapi.mapper;

import com.herysson.userphoneapi.dto.CreatePhoneRequestDTO;
import com.herysson.userphoneapi.dto.PhoneResponseDTO;
import com.herysson.userphoneapi.dto.UpdatePhoneRequestDTO;
import com.herysson.userphoneapi.model.Phone;

/**
 * Exercício 3 – Classe de mapeamento para Phone
 *
 * Optei pelo mapeamento manual porque o projeto é simples e não
 * justifica adicionar uma dependência extra como MapStruct agora.
 * Se o sistema crescer muito, aí vale a pena migrar.
 *
 * Todos os métodos são estáticos para não precisar instanciar a classe —
 * ela não guarda estado nenhum, então faz sentido.
 */
public class PhoneMapper {

    // Construtor privado para deixar claro que essa classe não deve ser instanciada
    private PhoneMapper() {}

    /**
     * Converte uma entidade Phone em PhoneResponseDTO.
     * Usado para montar a resposta que vai para o cliente.
     *
     * Direção: Entidade → DTO (saída)
     */
    public static PhoneResponseDTO toDTO(Phone phone) {
        return new PhoneResponseDTO(
                phone.getId(),
                phone.getNumber(),
                phone.getType()
        );
    }

    /**
     * Converte um CreatePhoneRequestDTO em entidade Phone.
     * O vínculo com o usuário (setUser) é feito no service,
     * porque o mapper não deveria conhecer o contexto de quem está criando.
     *
     * Direção: DTO (entrada) → Entidade
     */
    public static Phone toEntity(CreatePhoneRequestDTO dto) {
        Phone phone = new Phone();
        phone.setNumber(dto.number());
        phone.setType(dto.type());
        // id fica null — o banco vai gerar automaticamente
        return phone;
    }

    /**
     * Exercício 7 – Mapeamento reverso para update
     *
     * Em vez de criar um novo objeto Phone, atualizo os campos
     * do Phone que já existe no banco. Assim o JPA consegue
     * fazer o UPDATE corretamente sem perder o relacionamento com o usuário.
     *
     * Direção: DTO (entrada) → atualiza Entidade existente
     */
    public static void updateEntityFromDTO(UpdatePhoneRequestDTO dto, Phone phone) {
        // só atualiza se o valor vier preenchido — evita sobrescrever com null sem querer
        if (dto.number() != null) {
            phone.setNumber(dto.number());
        }
        if (dto.type() != null) {
            phone.setType(dto.type());
        }
    }
}
