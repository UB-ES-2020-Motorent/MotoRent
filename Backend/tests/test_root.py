from tests import BaseTestClass

class RootTestCase(BaseTestClass):

    def test_root(self):
        request = self.client.get('/')
        self.assertEqual(request.status_code, 200)
        self.assertEqual(request.data, b'MotoRent DataBase.\n Testing Travis...')



