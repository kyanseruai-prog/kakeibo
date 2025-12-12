# 家計簿アプリ

Java Spring Bootで構築された家計簿管理アプリケーションです。個人の収支管理に加えて、恋人や家族とグループで支出を共有し、割り勘計算も可能です。

## 主な機能

### 基本機能
- ユーザー登録・ログイン
- 収入・支出の登録・編集・削除
- カテゴリ別の分類（食費、交通費、娯楽費など）
- 月次/年次の集計表示
- 収支グラフ表示

### グループ共有機能
- グループの作成・管理
- 複数グループへの所属（恋人グループ、家族グループなど）
- グループメンバーとの取引共有
- メールアドレスでの招待

### 割り勘機能
- グループごとのデフォルト割り勘比率設定
- 支出登録時の個別比率変更
- 自動的な端数処理

## 技術スタック

- **バックエンド**: Java 17, Spring Boot 3.2.0
- **データベース**: PostgreSQL
- **フロントエンド**: Thymeleaf, jQuery, Bootstrap 5
- **認証**: Spring Security (Session-based)
- **その他**: Spring Data JPA, Lombok

## 必要な環境

- JDK 17以上
- Maven 3.8以上
- PostgreSQL 14以上

## ローカル環境でのセットアップ

### 1. リポジトリのクローン

```bash
git clone <repository-url>
cd kakeibo
```

### 2. PostgreSQLデータベースの作成

```sql
CREATE DATABASE kakeibo_db;
CREATE USER kakeibo_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE kakeibo_db TO kakeibo_user;
```

### 3. application.propertiesの設定

`src/main/resources/application.properties`を編集：

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/kakeibo_db
spring.datasource.username=kakeibo_user
spring.datasource.password=your_password
```

### 4. アプリケーションの起動

```bash
mvn clean install
mvn spring-boot:run
```

### 5. ブラウザでアクセス

```
http://localhost:8080
```

## Renderでのデプロイ

### 前提条件
- Renderアカウントの作成
- GitHubリポジトリへのプッシュ

### デプロイ手順

1. **Renderダッシュボードにアクセス**
   - [Render](https://render.com/)にログイン

2. **新しいWeb Serviceを作成**
   - 「New」→「Web Service」を選択
   - GitHubリポジトリを接続

3. **設定**
   - **Name**: kakeibo-app（任意）
   - **Environment**: Java
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/kakeibo-1.0.0.jar`
   - **Instance Type**: Free

4. **PostgreSQLデータベースの作成**
   - 「New」→「PostgreSQL」を選択
   - **Name**: kakeibo-db（任意）
   - **Database**: kakeibo_db
   - **User**: kakeibo_user
   - **Region**: 同じリージョンを選択
   - **Plan**: Free

5. **環境変数の設定**

   Web Serviceの「Environment」タブで以下を設定：

   ```
   DATABASE_URL=<PostgreSQLの内部接続URL>
   COOKIE_SECURE=true
   ```

6. **デプロイ**
   - 「Create Web Service」をクリック
   - 自動的にビルド・デプロイが開始されます

### デプロイ後の確認

- Renderが提供するURLにアクセス（例：`https://kakeibo-app.onrender.com`）
- 新規登録からユーザーを作成
- ログインして動作確認

## 使い方

### 1. ユーザー登録
1. トップページの「新規登録」をクリック
2. メールアドレス、ユーザー名、パスワードを入力
3. 「登録」ボタンをクリック

### 2. 取引の登録
1. ダッシュボードの「新規登録」ボタンをクリック
2. 取引タイプ（収入/支出）を選択
3. カテゴリ、金額、日付、メモを入力
4. 「保存」ボタンをクリック

### 3. グループの作成
1. ナビゲーションメニューの「グループ」をクリック
2. 「新規作成」ボタンをクリック
3. グループ名と説明を入力
4. 「作成」ボタンをクリック

### 4. グループでの取引
1. 取引登録画面で「グループ」を選択
2. グループ取引として登録されます

## プロジェクト構成

```
kakeibo/
├── pom.xml                                 # Maven設定
├── render.yaml                             # Renderデプロイ設定
├── README.md                               # このファイル
└── src/
    └── main/
        ├── java/com/example/kakeibo/
        │   ├── KakeiboApplication.java     # メインクラス
        │   ├── config/                     # 設定クラス
        │   ├── controller/                 # コントローラー
        │   ├── dto/                        # DTO
        │   ├── entity/                     # エンティティ
        │   ├── enums/                      # 列挙型
        │   ├── repository/                 # リポジトリ
        │   └── service/                    # サービス
        └── resources/
            ├── application.properties      # アプリケーション設定
            ├── data.sql                    # 初期データ
            ├── static/                     # 静的リソース
            │   ├── css/
            │   └── js/
            └── templates/                  # Thymeleafテンプレート
                ├── auth/                   # 認証画面
                ├── dashboard/              # ダッシュボード
                ├── group/                  # グループ
                ├── layout/                 # レイアウト
                └── transaction/            # 取引
```

## 開発

### ビルド

```bash
mvn clean package
```

### テスト実行

```bash
mvn test
```

### コードのフォーマット

```bash
mvn spring-javaformat:apply
```

## トラブルシューティング

### データベース接続エラー
- PostgreSQLが起動しているか確認
- `application.properties`の接続情報を確認
- ファイアウォール設定を確認

### ビルドエラー
- JDK 17以上がインストールされているか確認
- `mvn clean install`で依存関係を再取得

### Renderデプロイエラー
- ビルドログを確認
- 環境変数が正しく設定されているか確認
- データベース接続URLが正しいか確認

## ライセンス

MIT License

## サポート

問題が発生した場合は、GitHubのIssuesで報告してください。
