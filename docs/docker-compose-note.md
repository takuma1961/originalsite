version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: my-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword  # root ユーザーのパスワード
      MYSQL_DATABASE: demo_db            # 作成するデータベース名
      MYSQL_USER: demo_user              # 作成するユーザー
      MYSQL_PASSWORD: demo_password      # ユーザーのパスワード
    ports:
      - "3007:3306"  # ローカルPCの3007番ポートをMySQLの3306番ポートにマッピング
    volumes:
      - mysql_data:/var/lib/mysql
    command: --default-authentication-plugin=mysql_native_password

volumes:
  mysql_data:
