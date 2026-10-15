# Créer et publier le dépôt GitHub

## 1. Créer le dépôt vide

Sur GitHub, se connecter puis choisir **+ → New repository**.
Nom proposé : `tp-jwt-spring`. Choisir Public pour un portfolio, ou Private selon
les consignes de l’établissement. Coller la description de `description-github.txt`.
Ne pas ajouter de README, de .gitignore ou de licence lors de cette création,
puis cliquer sur **Create repository**. Choisir une licence séparément si les
droits sur le TP et le code pédagogique le permettent ; aucune licence n’est
inventée dans cette préparation.

## 2. Depuis le dossier contenant pom.xml

Le projet préparé peut déjà posséder ses commits locaux. Vérifier d’abord :

```sh
git status
git log --oneline -3
```

Si le dépôt n’a pas encore été initialisé (par exemple après extraction du ZIP) :

```sh
git init -b main
git add .
git diff --cached --stat
git commit -m "feat: authentification JWT et tests API"
```

La commande `git add .` respecte `.gitignore`, qui exclut `.env`, `target` et
les fichiers IntelliJ locaux. Vérifier les fichiers avant de publier :

```sh
git ls-files
git check-ignore .env
```

## 3. Envoyer vers GitHub

Remplacer `VOTRE_COMPTE` par son identifiant GitHub :

```sh
git branch -M main
git remote add origin https://github.com/VOTRE_COMPTE/tp-jwt-spring.git
git push -u origin main
```

Si `origin` existe déjà, vérifier `git remote -v` avant de le modifier. GitHub
peut demander une authentification via Git Credential Manager : effectuer cette
étape soi-même et ne jamais coller de token personnel dans le code ou dans une URL.

## 4. Après publication

Vérifier le README, les images et la collection Postman sur la page du dépôt.
Ajouter la courte description et, si souhaité, les sujets `java`, `spring-boot`,
`spring-security`, `jwt`, `rest-api` et `postman`. Aucun dépôt distant n’est créé
automatiquement et aucune publication n’est effectuée sans destination connue.
