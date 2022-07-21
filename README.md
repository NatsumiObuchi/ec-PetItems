# ECサイト　らくらくペット
らくらくペットでは、ペット用品販売ECサイトを想定し作成しました。
今後も実装機能を追加していく予定です。

## 目次
- [使用ツール](https://github.com/rpentry202204w/ec-202204b##使用ツール)
- [セットアップ](https://github.com/rpentry202204w/ec-202204b##セットアップ)
- [実装機能](https://github.com/rpentry202204w/ec-202204b##実装機能)

## 使用ツール
*フロント*
- HTML 5
- CSS 3
- jQuery(1.11.3)

*サーバー*
- Java(openjdk 18.0.1.1)
- Springboot(2.7.0)
- Thymeleaf
- JavaScript

*データベース*
- PostgreSQL

*テスト*
- JUnit 5

## セットアップ
### Javaインストール
openjdk(18.0.1.1)を使用しています。  
インストールは[こちら](https://jdk.java.net/18/)から。

### Spring Tool Suiteインストール
Spring Tools 4 for Eclipseを使用しています。（最新バージョンは4.15.1です。）  
インストールは[こちら](https://spring.io/tools)から。

### データベース
PostgeSQLを使用しています。  
「student」という名前のデータベースを作成し、[こちら](https://docs.google.com/document/d/1qPmDEEQ5emsmlowiZsx1e-v_p-lIZqphPEnjqm9M5EI/edit)のSQLを実行してください。  

ER図は下記の通りです。  
<details>
  <summary>ER図</summary>  
  
![coupon drawio (1)](https://user-images.githubusercontent.com/105257893/180108231-c56c4d5b-c3d2-438f-88e4-97ca65a42002.png)

  
</details>

## 実装機能
<details>
  <summary>サイトマップ</summary>
  
  ![サイトマップ](./img/sitemap.png)
</details>

<details>
  <summary>ユーザー登録・入力値チェック・ログイン・ログアウト機能</summary>
  準備中
</details>

<details>
  <summary>新規登録フォームの郵便番号入力部分の住所検索機能</summary>
  準備中
</details>

<details>
  <summary>ページング機能</summary>
  商品一覧画面において、jQueryを使用してページングをする機能を実装しました。
</details>

<details>
  <summary>商品検索・オートコンプリート機能</summary>
  ページ上部に商品検索フォームを設置しています。  
  フォームに文字を入力すると、それが含まれる商品名が候補として表示されます。  
  ![オートコンプリート](./img/autoComplete.jpg)
</details>

<details>
  <summary>並び替え機能</summary>
  新着順をデフォルトとして、現在選択しているカテゴリ内などでも並び替えができます。
  現在選択している並び替え順がわかりやすいように、選択した並び替え順は非リンク化するように実装しました。
</details>

<details>
  <summary>商品レビュー機能</summary>
  各商品詳細ページから、星を使った評価とレビューコメントができます。
  星を使った評価の平均を表示し、ユーザーが商品の評価を可視化できるようにしています。
  ※自分のレビュー履歴は、マイページから見ることができます。
</details>

<details>
  <summary>商品お気に入り機能</summary>
  各商品詳細ページから、商品をお気に入り登録することができます。
  ※自分のお気に入りリストは、ヘッダーまたはマイページから見ることができます。
</details>

<details>
  <summary>ポイント機能</summary>
  準備中
</details>

<details>
  <summary>クーポン機能</summary>
  ユーザーが取得したクーポンを、決済画面で使用することができます。
  クーポンの有効期限が切れた場合は、ユーザーのログイン時にクーポン情報をDBから削除するようにしています。
  
  ※現在の仕様だとユーザーがクーポンを大量に取得できてしまうので、今後改善予定。
</details>

<details>
  <summary>商品の数量と、クーポン・ポイント使用時に連動する金額の動的変更処理</summary>
  jQueryを使用して、ユーザーの処理に合わせて動的に金額が変化します。
</details>

<details>
  <summary>クレジット決済における有効性チェック機能</summary>
  準備中
</details>

<details>
  <summary>注文完了自動送信メール送信機能</summary>
  準備中
</details>

<details>
  <summary>お届け先情報登録機能</summary>
  準備中
</details>

<details>
  <summary>登録者情報変更・退会機能</summary>
  準備中
</details>
