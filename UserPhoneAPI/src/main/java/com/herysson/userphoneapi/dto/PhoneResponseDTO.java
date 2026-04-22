package com.herysson.userphoneapi.dto;

/**
 * Exercício 1 – DTO de saída para Phone
 *
 * Usando record aqui porque ele já gera construtor, getters, equals,
 * hashCode e toString automaticamente — não preciso escrever nada disso na mão.
 *
 * A ideia é que o cliente da API receba apenas id, number e type.
 * O campo user (entidade JPA com todo o relacionamento) fica escondido,
 * evitando tanto dados desnecessários quanto possíveis loops de serialização.
 */
public record PhoneResponseDTO(
        Long id,
        String number,
        String type
) {}
