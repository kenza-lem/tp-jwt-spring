# Alignement avec la deuxième partie du support

Référence : Introduction-18-33.pdf, 16 pages, fourni par l’utilisateur.

Les dépendances JJWT 0.13.0, AppUser, les DTO, CustomUserDetailsService,
JwtService et le filtre étaient déjà présents. Les comportements suivants
ont été alignés sur les pages 5, 13, 14 et 16 :

- Ajout de UserRepository.existsByEmail.
- POST /auth/register : 200 « Utilisateur cree » ; doublon 400 « Email deja utilise ».
- GET /api/hello : 200 « Bienvenue dans l'API securisee » avec JWT valide.
- GET /api/profile : 200 « Utilisateur connecte : <email> » avec JWT valide.
- Les routes /api/** exigent une authentification, sans rôle particulier imposé.
- Les autres routes suivent la règle permitAll du support ; les routes inexistantes
  ne deviennent pas des endpoints et peuvent renvoyer 404.

Les noms de projet et de package existants sont conservés. Les constructeurs et
accesseurs restent explicites. La base reste nommée securitydbdemo localement.
La clé JWT est conservée hors du code versionné et la console H2 reste activable
localement. Le filtre conserve sa gestion explicite des JWT invalides/expirés (401),
plutôt qu’un catch général silencieux. La validation de saisie existante est conservée.

La collection Postman et les tests Java ont été adaptés à ces contrats.
Les preuves de l’ancienne version sont archivées et ne prouvent pas cette version.
Les captures personnelles de Postman seront réalisées par l’utilisateur avec le guide.
