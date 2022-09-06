# ECサイト　らくらくペット
らくらくペットでは、ペット用品販売ECサイトを想定し作成しました。
今後も実装機能を追加していく予定です。

![image](https://user-images.githubusercontent.com/105257893/180350354-a4a5d33d-6dd4-46a4-862f-4ac60beda0a1.png)


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
「student」という名前のデータベースを作成し、[こちら](https://docs.google.com/spreadsheets/d/1Mr0rphtDlUzo05eS7zZjsYN2b0vL47I9n2LzVEogJ4Y/edit#gid=0)のSQLを実行してください。  

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
ユーザーが新規会員登録→ログイン→ログアウトをすることができます。その際、入力フォームを空欄にしたり、誤った形式の入力をした場合、バリデーションチェックが入りエラー文が返されます。

![image](https://user-images.githubusercontent.com/105257893/180353212-5087814d-a478-4962-9867-663c421212cf.png)

</details>

<details>
  <summary>新規登録フォームの郵便番号入力部分の住所検索機能</summary>

[ZIPCODA API](https://zipcoda.net/doc)
を利用し、郵便番号の数値を入力し住所検索ボタンを押下すると、対応する住所が反映されるよう実装しました。

![image](https://user-images.githubusercontent.com/105257893/180353650-403b9f0c-ee5e-49d4-9cee-73f5fbc8cf97.png)


</details>

<details>
  <summary>ページング機能</summary>
  商品一覧画面において、jQueryを使用してページングをする機能を実装しました。
  
  ![image](https://user-images.githubusercontent.com/105257893/180350856-57e2d55e-9c33-4b01-80de-1b2725aa0439.png)
  
</details>

<details>
  <summary>商品検索・オートコンプリート機能</summary>
  ページ上部に商品検索フォームを設置しています。  
  フォームに文字を入力すると、それが含まれる商品名が候補として表示されます。  
  
  ![image](https://user-images.githubusercontent.com/105257893/180350723-5fac5c19-183a-4d81-91be-bdbaf73c0335.png)
  
</details>

<details>
  <summary>並び替え機能</summary>
  新着順をデフォルトとして、現在選択しているカテゴリ内などでも並び替えができます。
  現在選択している並び替え順がわかりやすいように、選択した並び替え順は非リンク化するように実装しました。
  
  ![image](https://user-images.githubusercontent.com/105257893/180350928-3326f2f4-60a4-4b98-8c1f-2b39177dd9b9.png)

</details>

<details>
  <summary>商品レビュー機能</summary>
  各商品詳細ページから、星を使った評価とレビューコメントができます。
  
  ![image](https://user-images.githubusercontent.com/105257893/180351156-0041e21b-dbeb-44c4-91d6-633b5f60b89c.png)

  星を使った評価の平均を表示し、ユーザーが商品の評価を可視化できるようにしています。
  
  ![image](https://user-images.githubusercontent.com/105257893/180351317-964e05c5-aea3-4775-bae3-09e4f3e9a333.png)
  
  ※自分のレビュー履歴は、マイページから見ることができます。
  
  ![image](https://user-images.githubusercontent.com/105257893/180351520-048416e8-1d81-4263-96c3-b890b7f5c293.png)  
  
</details>

<details>
  <summary>商品お気に入り機能</summary>
  各商品詳細ページから、商品をお気に入り登録することができます。
  
  ![image](https://user-images.githubusercontent.com/105257893/180351724-4e8e1d5f-7bd0-45e7-9c3b-a034dc202c15.png)
  
  ※自分のお気に入りリストは、ヘッダーまたはマイページから見ることができます。
  
  ![image](https://user-images.githubusercontent.com/105257893/180351860-975640d9-9276-4b90-9624-4cd1a89c52f4.png)
  
</details>

<details>
  <summary>ポイント機能</summary>
  準備中
</details>

<details>
  <summary>クーポン機能</summary>
  ユーザーが取得したクーポンを、決済画面で使用することができます。
  クーポンの有効期限が切れた場合は、ユーザーのログイン時にクーポン情報をDBから削除するようにしています。
  
  ![image](https://user-images.githubusercontent.com/105257893/180352138-a2861c13-1e7d-439f-a9e5-ab469e2c79e2.png)
  
  
  ※取得済みのクーポンは、マイページから見ることができます。

![image](https://user-images.githubusercontent.com/105257893/180352202-21eceec4-fd53-45a6-b4f8-d21ed87d92df.png)

  
  **※現在の仕様だとユーザーがクーポンを大量に取得できてしまうので、今後改善予定。**
</details>

<details>
  <summary>商品の数量と、クーポン・ポイント使用時に連動する金額の動的変更処理</summary>
  jQueryを使用して、ユーザーの処理に合わせて動的に金額が変化します。
  
  ![image](https://user-images.githubusercontent.com/105257893/180352389-6eef7a14-b6ea-4911-b944-23bc583c45fb.png)
</details>

<details>
  <summary>クレジット決済における有効性チェック機能</summary>
  準備中
  
  ![image](https://user-images.githubusercontent.com/105257893/180352460-c2402dae-3cce-48e2-b641-d2c3e820c5de.png)
  
</details>

<details>
  <summary>注文完了自動送信メール送信機能</summary>
  準備中
</details>

<details>
  <summary>お届け先情報登録機能</summary>
  注文確認画面で、ユーザーが設定したデフォルトのお届け先情報を反映させることができます。
  
  ![image](https://user-images.githubusercontent.com/105257893/180352630-5e8116c4-5127-45bb-aa21-4e275fa03b40.png)

　ユーザーはお届け先情報を最大３つまで登録することができ、注文確認画面で選択することができます。

![image](https://user-images.githubusercontent.com/105257893/180352822-ea0c625a-913f-4088-b23e-36b4798219eb.png)

</details>

<details>
  <summary>ユーザー情報変更・退会機能</summary>
  ユーザーがマイページから登録した情報を変更したり、退会することができます。

![image](https://user-images.githubusercontent.com/105257893/180352964-813f163d-def9-422e-9e8a-f762fb46edc2.png)

</details>
