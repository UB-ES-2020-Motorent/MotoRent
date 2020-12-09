from tests import BaseTestClass
import json


class MyTestCase(BaseTestClass):

    def test_post_user(self):
        sent = {'mail': 'joan1123@gmail.com',
                'google_token': 'da23d45thgf4567ujkloiuy678io0iuhgvcxdertgygf5y9i',
                'role': 0}
        request = self.client.post('/user', data=sent)
        my_json = json.loads(request.data.decode('utf8').replace("'", '"'))
        mail = my_json['user']['mail']
        google_token = my_json['user']['google_token']
        role = my_json['user']['role']
        self.assertEqual(request.status_code, 201)
        self.assertEqual(mail, sent['mail'])
        self.assertEqual(google_token, sent['google_token'])
        self.assertEqual(role, sent['role'])

    def test_get_user_id(self):
        id = 1
        request = self.client.get('/user?id=' + str(id) + '&admin_code=admin_secret_code')
        my_json = json.loads(request.data.decode('utf8').replace("'", '"'))
        id_requested = my_json['user']['id']
        self.assertEqual(request.status_code, 200)
        self.assertEqual(id, id_requested)

    def test_get_user_google_token(self):
        google_token = 'juy65rfty76Hg65FVytfGGDD63ccxeDFg'
        request = self.client.get('/user?google_token=' + google_token + '&admin_code=admin_secret_code')
        my_json = json.loads(request.data.decode('utf8').replace("'", '"'))
        print(my_json)
        google_token_requested = my_json['user']['google_token']
        self.assertEqual(request.status_code, 200)
        self.assertEqual(google_token, google_token_requested)

    def test_get_all_users(self):
        request = self.client.get('/users')
        self.assertEqual(request.status_code, 200)

    def test_wrong_admin_code(self):
        request = self.client.get('/users?admin_code=wrong_admin_code')
        self.assertEqual(request.status_code, 400)

    def test_put_user(self):
        id = 1
        new_name = 'Johan'
        request = self.client.put('user/' + str(id) +'?admin_code=admin_secret_code&name=' + new_name)
        my_json = json.loads(request.data.decode('utf8').replace("'", '"'))
        print(my_json)
        self.assertEqual(request.status_code, 200)
        self.assertEqual(new_name, my_json['user']['name'])
        request = self.client.get('/user?id=' + str(id) + '&admin_code=admin_secret_code')
        my_json = json.loads(request.data.decode('utf8').replace("'", '"'))
        self.assertEqual(request.status_code, 200)
        self.assertEqual(new_name, my_json['user']['name'])

    def test_delete_user(self):
        id = 1
        request = self.client.delete('/user/' + str(id) + '?admin_code=admin_secret_code')
        self.assertEqual(request.status_code, 200)
        request = self.client.get('/user?id=' + str(id) + '&admin_code=admin_secret_code')
        self.assertEqual(request.status_code, 404)


