import { Outlet, NavLink } from 'react-router-dom'
import { Home, Plus, MessageCircle, User } from 'lucide-react'
import { useThreads } from '@/features/chat'

export default function AppLayout() {
  const { data: threads } = useThreads()
  const totalUnread = threads?.reduce((n, t) => n + t.unreadCount, 0) ?? 0

  return (
    <div className="flex min-h-screen flex-col">
      <div className="flex-1 pb-16">
        <Outlet />
      </div>

      <nav className="fixed bottom-0 left-0 right-0 z-30 flex border-t border-gray-200 bg-white">
        <NavItem to="/" icon={Home} label="Home" end />
        <NavItem to="/listings/new" icon={Plus} label="Sell" />
        <NavItem to="/chat" icon={MessageCircle} label="Messages" badge={totalUnread} />
        <NavItem to="/profile/me" icon={User} label="Profile" />
      </nav>
    </div>
  )
}

interface NavItemProps {
  to: string
  icon: React.ElementType
  label: string
  badge?: number
  end?: boolean
}

function NavItem({ to, icon: Icon, label, badge, end }: NavItemProps) {
  return (
    <NavLink
      to={to}
      end={end}
      className={({ isActive }) =>
        `relative flex flex-1 flex-col items-center justify-center gap-0.5 py-2 text-xs transition-colors ${
          isActive ? 'text-primary-600' : 'text-gray-500 hover:text-gray-800'
        }`
      }
    >
      <div className="relative">
        <Icon className="h-5 w-5" />
        {badge != null && badge > 0 && (
          <span className="absolute -right-1.5 -top-1.5 flex h-4 min-w-4 items-center justify-center rounded-full bg-red-500 px-0.5 text-[10px] font-bold text-white">
            {badge > 99 ? '99+' : badge}
          </span>
        )}
      </div>
      <span>{label}</span>
    </NavLink>
  )
}
