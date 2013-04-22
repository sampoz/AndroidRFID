import web
from web import form

db = web.database(dbn='mysql', user='backend', pw='backbackend', db='androidrfid', host='sampoz.dyndns.org', port=3306)

render = web.template.render('templates/')

urls = (
    '/', 'Index',
    '/login', 'Login',
    '/logout', 'Logout',
    '/listusers', 'Listusers',
    '/add', 'Add',
    '/finduser', 'Finduser'
)

web.config.debug = False;

app = web.application(urls, locals())
session = web.session.Session(app, web.session.DiskStore('sessions'))

class Index:
    def GET(self):
        if session.get('logged_in', False):
            return render.index()
        else:
            #return '<h1>You are not logged in</h1><a href="/login">Login now</a>'
            raise web.seeother('/login')

class Login:
    loginForm = form.Form(
        form.Textbox('username'),
        form.Password('password'),
        form.Button('login'),
    )
    def GET(self):
        l = self.loginForm()
        return render.login(l.render())

    def POST(self):
        i, j = web.input().username, web.input().password
        user = db.query("SELECT * FROM ADMIN WHERE LOGIN='" + i + "' AND PASSWORD='" + j + "'")
        print user[0].LOGIN
        if i != '':
            session.logged_in = True;
            raise web.seeother('/')
        else:
            return '<h1>False user/pw</h1>'

class Logout:
    def GET(self):
        session.logged_in = False
        raise web.seeother('/')

class Listusers:
    def GET(self):
        users = db.select('USER')
        return render.listusers(users)

class Add:
    def POST(self):
        i = web.input()
        n = db.insert('USER', NAME=i.name, MONEY=i.money)
        raise web.seeother('/')

class Finduser:
    def POST(self):
        i = web.input()
        n = db.query("SELECT USER.NAME name FROM IDCARD JOIN USER WHERE CARDID='" + i.cardID + "' AND USERID=USER.ID")
        m = db.query("SELECT USER.MONEY money FROM IDCARD JOIN USER WHERE CARDID='" + i.cardID + "' AND USERID=USER.ID")
        name = n[0].name
        mon = m[0].money
        return name, mon

if __name__ == "__main__":
    app.run()
