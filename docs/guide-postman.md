# Réaliser soi-même les tests et captures Postman

## Préparer le projet

Dans IntelliJ, ouvrir le dossier contenant pom.xml, recharger Maven et lancer
TpJwtSpringApplication.main(). Attendre « Tomcat started on port 8080 ».
Garder le serveur démarré. Après chaque redémarrage, refaire l’inscription : H2 est en mémoire.

## 1. Inscription

Créer une requête POST http://localhost:8080/auth/register.
Dans Body → raw → JSON, saisir un e-mail de test et un mot de passe de test :
`{"email":"mon-test@example.com","password":"<mot de passe de test choisi>"}`.
Choisir Authorization → No Auth. Cliquer sur Send.
Résultat : 200 OK et « Utilisateur cree ».
Capture à faire : URL, méthode, code HTTP et message ; masquer le mot de passe.

## 2. Connexion

Créer POST http://localhost:8080/auth/login, avec le même JSON.
Résultat : 200 OK et {"token":"..."}.
Copier le token pour les prochaines étapes ; masquer sa valeur avant toute publication.
Ne pas publier la capture brute montrant le token complet.

## 3. Hello

Créer GET http://localhost:8080/api/hello. Aucun body.
Authorization → Bearer Token, coller le token sans ajouter le mot Bearer.
Résultat : 200 OK et « Bienvenue dans l'API securisee ».
Capturer l’URL et la réponse ; éviter de laisser le champ Token visible.

## 4. Profil

Créer GET http://localhost:8080/api/profile, avec le même Bearer Token.
Résultat : 200 OK et « Utilisateur connecte : mon-test@example.com ».
Le profil renvoie du texte, conformément au PDF, et non un objet JSON.

## 5. Cas d’erreur

- Sur le profil, choisir No Auth : 401 Unauthorized.
- Essayer un token invalide : 401 Unauthorized.
- Refaire l’inscription avec le même e-mail : 400 Bad Request et « Email deja utilise ».
- Essayer une connexion avec un mauvais mot de passe : 401 Unauthorized.

## 6. Collection automatisée

Importer postman/TP-JWT.postman_collection.json et postman/Local.postman_environment.json.
Sélectionner l’environnement Local puis exécuter toute la collection dans l’ordre.
Elle génère ses propres données temporaires ; ne pas mélanger ces comptes avec les tests manuels.
Capturer le bilan du Runner une fois les tests terminés.

## Enregistrer ses captures

Enregistrer ses propres images dans screenshots/ sous les noms :
01-inscription.png, 02-connexion-masquee.png, 03-hello.png, 04-profil.png,
05-acces-refuse.png et 06-runner.png. Ne pas simuler les réponses ou les résultats.
L’accompagnement dans la conversation se fera une requête à la fois, à partir
de ce que l’utilisateur voit réellement dans Postman.
