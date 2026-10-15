# Analyse de l’enregistrement du 15 octobre 2026

Durée du fichier fourni : 1 h 42 min 08 s environ. Analyse visuelle des écrans
de code, diapositives et démonstrations ; aucune transcription audio n’a été produite.

| Repère approximatif | Éléments observés | État du projet |
|---|---|---|
| 04–13 min | Connexion, création du JWT, accès à une ressource protégée | Parcours déjà implémenté |
| 17–30 min | JwtService et CustomUserDetailsService | Présents, compatibles JJWT 0.13.0 |
| 34–60 min | Filtre JWT, en-tête Bearer et SecurityContextHolder | Présents dans JwtAuthenticationFilter |
| 63–81 min | BCrypt, AuthenticationManager, SecurityFilterChain stateless | Présents ; API adaptée à Spring Security 7 |
| 85–94 min | AuthController, inscription USER et connexion | Présents ; message d’inscription aligné |
| 98–102 min | Démarrage et requêtes Postman | Vérification indépendante avec tests Java/Newman |

La méthode GET est retenue pour /api/profile, conformément aux schémas et à
UserController. Un écran Postman final montre POST sur cette URL en cours de
préparation ; il ne constitue pas la preuve d’un endpoint POST /api/profile.

## Différences assumées

- Constructeurs et accesseurs AppUser explicites après le problème Lombok rencontré.
- Le filtre renvoie 401 pour un JWT invalide au lieu d’ignorer toutes les exceptions.
- Retour 409 pour un email déjà inscrit et 400 pour une saisie invalide.
- Le profil exige le rôle USER ; cela conserve la règle du projet préparé précédemment.
- La clé JWT est externalisée et la console H2 est désactivée par défaut.
- Conservation du package controller singulier, sans renommage superflu.

## Ce qui n’est pas démontré ni ajouté

Pas d’interface graphique, de CRUD utilisateur, de refresh token, de révocation
ou de récupération de mot de passe dans le périmètre vérifié. Aucune route
PUT, PATCH ou DELETE n’est inventée. Aucun résultat de la vidéo n’est présenté
comme un test exécuté sur ce projet.
