(ns blackjackv1.core
  (:require [card-ascii-art.core :as card]))
; A,2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K

(defn new-card  []
  "Generate a card number between 1 and 13"
 (inc (rand-int 13)))

; função para calcular os pontos
; regras de soma J, Q , K = 10 (não 11,12,13)
;[A 10] = 11 OU 21 = 21
;[A 5 7] = 1+5+7 (13) OU 11+5+7 (23)
; A = 11  PORÉM SE PASSAR DE 21 , VALE 1

;(reduce + 1 [2 3 4])
; (+1 2)= 3
; (+3 3)= 6
; (+6 4)= 4

(defn JQK->10 [card]
  (if (> card 10) 10 card))

(defn A->11 [card]
  (if (= card 1)11 card))

(defn points-cards [cards]
  (let [cards-without-JQK (map JQK->10 cards)
        cards-with-A11 (map A->11 cards-without-JQK)
        points-with-A-1 (reduce + cards-without-JQK)
        points-with-A-11 (reduce + cards-with-A11)]
    (if (> points-with-A-11 21) points-with-A-1 points-with-A-11)))


; como representar o jogador
; {:player "Sérgio Vinnicius"
;  :cards [3 4]}

(defn player [player-name]
 (let [card1 (new-card)
       card2 (new-card)
       cards [card1 card2]
       points (points-cards cards)]
   {:player-name player-name
    :cards cards
    :points  points}))

; call the function o new card e genaration  new card
; atualizar o vetor cards dentro do player com a new card
; calcular pontos com o novo jogador e com o vetor cards
; retonar novo jogador


(defn more-card [player]
  (let [card (new-card)
       cards (conj (:cards player) card)
       new-player (update player :cards conj card)
       points (points-cards cards)]
  (assoc new-player :points points)))

; função para automatizar dealer

(defn player-decision-continue? [player]
  (= (read-line) "sim"))

(defn dealer-decision-continue? [player-points dealer]
  (let [dealer-points(:points dealer)]
    (if (> player-points 21) false (<= dealer-points player-points))))


;função para solicitar mais cartas

(defn game [player fn-decision-continue?]
  (println (:player-name player) ": mais cartas?")
  (if (fn-decision-continue? player)
    (let [player-with-more-cards (more-card player)]
      (card/print-player player-with-more-cards)
      (game player-with-more-cards fn-decision-continue?))
    player))

;função criada para sinalizar quem venceu

(defn end-game [player dealer]
  (let [player-points (:points player)
        dealer-points (:points dealer)
        player-name (:player-name player)
        dealer-name (:dealer-name dealer)
        message (cond
                  (and (> player-points 21 ) (> dealer-points 21)) "Ambos perderam"
                  (= player-points dealer-points ) "empatou"
                  (> player-points 21) (str dealer-name " ganhou")
                  (> dealer-points 21) (str player-name " Dealer ganhou")
                  (> player-points dealer-points) (str player-name "win")
                  (> dealer-points player-points) (str dealer-name " Dealer win"))]
    (card/print-player player)
    (card/print-player dealer)
    (print message)))

(def player-1 (player "Sérgio"))
(card/print-player player-1)

(def dealer (player "Dealer"))
(card/print-masked-player dealer)

(def player-after-game (game player-1 player-decision-continue? ))
(def partial-dealer-decision-continue? (partial dealer-decision-continue? (:points player-after-game)))
(def dealer-after-game (game dealer partial-dealer-decision-continue?))


(end-game player-after-game dealer-after-game)




