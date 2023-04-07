package database_service.repositories
import config.util.HikariUtils.db
import database_service.entity._
import database_service.repositories.CompanyRepository.userCompaniesCollectionTable
import slick.jdbc.H2Profile.api._




object UserRepository {
  lazy val users: TableQuery[UserTable] = TableQuery[UserTable]
  lazy val userAttributes: TableQuery[UserAttributeTable] = TableQuery[UserAttributeTable]

  def getUsers() = db.run{
    (for {
      u <- users
      ua <- userAttributes if u.userAttributeId === ua.id
    } yield (u, ua)).result
  }

  def getCompaniesIDs() = db.run{
    userCompaniesCollectionTable.result
  }

  def findUserByUsername(username: String) =
    db.run(users.filter(_.username === username).result.headOption)

  def saveUserAttribute(userAttribute: UserAttribute) = db.run{
    userAttributes += userAttribute
  }

  def saveUser(user: User) = db.run{
    users += user
  }

  def updateUser(user: User) = db.run{
    users.filter(_.id===user.user_id).update(user)
  }
  def updateUserAttribute(userAttribute: UserAttribute) = db.run{
    userAttributes.filter(_.id===userAttribute.userAttr_id).update(userAttribute)
  }
}
