(ns teste1)


; var nome = "Sérgio"
; var idade= 30;
; int idade = 30;

(def nome "Sérgio")
(def idade 30)
(def numeros [1 3 6 8])
(println numeros)

(defn saudacao "Saudação iniciante em Cloruje"
  [arg]
  (str "Bem vindo " arg))

(println (saudacao nome) )